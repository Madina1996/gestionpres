import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DepenseFormService, DepenseFormGroup } from './depense-form.service';
import { IDepense } from '../depense.model';
import { DepenseService } from '../service/depense.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

@Component({
  standalone: true,
  selector: 'jhi-depense-update',
  templateUrl: './depense-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepenseUpdateComponent implements OnInit {
  isSaving = false;
  depense: IDepense | null = null;

  fournisseursSharedCollection: IFournisseur[] = [];

  editForm: DepenseFormGroup = this.depenseFormService.createDepenseFormGroup();

  constructor(
    protected depenseService: DepenseService,
    protected depenseFormService: DepenseFormService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFournisseur = (o1: IFournisseur | null, o2: IFournisseur | null): boolean => this.fournisseurService.compareFournisseur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depense }) => {
      this.depense = depense;
      if (depense) {
        this.updateForm(depense);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depense = this.depenseFormService.getDepense(this.editForm);
    if (depense.id !== null) {
      this.subscribeToSaveResponse(this.depenseService.update(depense));
    } else {
      this.subscribeToSaveResponse(this.depenseService.create(depense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepense>>): void {
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

  protected updateForm(depense: IDepense): void {
    this.depense = depense;
    this.depenseFormService.resetForm(this.editForm, depense);

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(
      this.fournisseursSharedCollection,
      depense.fournisseur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, this.depense?.fournisseur)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));
  }
}
