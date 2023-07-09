import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CompteFormService, CompteFormGroup } from './compte-form.service';
import { ICompte } from '../compte.model';
import { CompteService } from '../service/compte.service';

@Component({
  standalone: true,
  selector: 'jhi-compte-update',
  templateUrl: './compte-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CompteUpdateComponent implements OnInit {
  isSaving = false;
  compte: ICompte | null = null;

  editForm: CompteFormGroup = this.compteFormService.createCompteFormGroup();

  constructor(
    protected compteService: CompteService,
    protected compteFormService: CompteFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compte }) => {
      this.compte = compte;
      if (compte) {
        this.updateForm(compte);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compte = this.compteFormService.getCompte(this.editForm);
    if (compte.id !== null) {
      this.subscribeToSaveResponse(this.compteService.update(compte));
    } else {
      this.subscribeToSaveResponse(this.compteService.create(compte));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompte>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(compte: ICompte): void {
    this.compte = compte;
    this.compteFormService.resetForm(this.editForm, compte);
  }
}
