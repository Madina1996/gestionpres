import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PresFormService, PresFormGroup } from './pres-form.service';
import { IPres } from '../pres.model';
import { PresService } from '../service/pres.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

@Component({
  standalone: true,
  selector: 'jhi-pres-update',
  templateUrl: './pres-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PresUpdateComponent implements OnInit {
  isSaving = false;
  pres: IPres | null = null;

  fournisseursSharedCollection: IFournisseur[] = [];

  editForm: PresFormGroup = this.presFormService.createPresFormGroup();

  constructor(
    protected presService: PresService,
    protected presFormService: PresFormService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFournisseur = (o1: IFournisseur | null, o2: IFournisseur | null): boolean => this.fournisseurService.compareFournisseur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pres }) => {
      this.pres = pres;
      if (pres) {
        this.updateForm(pres);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pres = this.presFormService.getPres(this.editForm);
    if (pres.id !== null) {
      this.subscribeToSaveResponse(this.presService.update(pres));
    } else {
      this.subscribeToSaveResponse(this.presService.create(pres));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPres>>): void {
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

  protected updateForm(pres: IPres): void {
    this.pres = pres;
    this.presFormService.resetForm(this.editForm, pres);

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(
      this.fournisseursSharedCollection,
      pres.fournisseur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, this.pres?.fournisseur)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));
  }
}
