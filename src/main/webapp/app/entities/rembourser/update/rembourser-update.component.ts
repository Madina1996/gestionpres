import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { RembourserFormService, RembourserFormGroup } from './rembourser-form.service';
import { IRembourser } from '../rembourser.model';
import { RembourserService } from '../service/rembourser.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

@Component({
  standalone: true,
  selector: 'jhi-rembourser-update',
  templateUrl: './rembourser-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RembourserUpdateComponent implements OnInit {
  isSaving = false;
  rembourser: IRembourser | null = null;

  fournisseursSharedCollection: IFournisseur[] = [];

  editForm: RembourserFormGroup = this.rembourserFormService.createRembourserFormGroup();

  constructor(
    protected rembourserService: RembourserService,
    protected rembourserFormService: RembourserFormService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFournisseur = (o1: IFournisseur | null, o2: IFournisseur | null): boolean => this.fournisseurService.compareFournisseur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rembourser }) => {
      this.rembourser = rembourser;
      if (rembourser) {
        this.updateForm(rembourser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rembourser = this.rembourserFormService.getRembourser(this.editForm);
    if (rembourser.id !== null) {
      this.subscribeToSaveResponse(this.rembourserService.update(rembourser));
    } else {
      this.subscribeToSaveResponse(this.rembourserService.create(rembourser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRembourser>>): void {
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

  protected updateForm(rembourser: IRembourser): void {
    this.rembourser = rembourser;
    this.rembourserFormService.resetForm(this.editForm, rembourser);

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(
      this.fournisseursSharedCollection,
      rembourser.fournisseur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, this.rembourser?.fournisseur)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));
  }
}
