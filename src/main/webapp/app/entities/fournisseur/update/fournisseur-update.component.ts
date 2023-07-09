import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FournisseurFormService, FournisseurFormGroup } from './fournisseur-form.service';
import { IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

@Component({
  standalone: true,
  selector: 'jhi-fournisseur-update',
  templateUrl: './fournisseur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FournisseurUpdateComponent implements OnInit {
  isSaving = false;
  fournisseur: IFournisseur | null = null;

  editForm: FournisseurFormGroup = this.fournisseurFormService.createFournisseurFormGroup();

  constructor(
    protected fournisseurService: FournisseurService,
    protected fournisseurFormService: FournisseurFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fournisseur }) => {
      this.fournisseur = fournisseur;
      if (fournisseur) {
        this.updateForm(fournisseur);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fournisseur = this.fournisseurFormService.getFournisseur(this.editForm);
    if (fournisseur.id !== null) {
      this.subscribeToSaveResponse(this.fournisseurService.update(fournisseur));
    } else {
      this.subscribeToSaveResponse(this.fournisseurService.create(fournisseur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFournisseur>>): void {
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

  protected updateForm(fournisseur: IFournisseur): void {
    this.fournisseur = fournisseur;
    this.fournisseurFormService.resetForm(this.editForm, fournisseur);
  }
}
