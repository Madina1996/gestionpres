import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFournisseur, NewFournisseur } from '../fournisseur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFournisseur for edit and NewFournisseurFormGroupInput for create.
 */
type FournisseurFormGroupInput = IFournisseur | PartialWithRequiredKeyOf<NewFournisseur>;

type FournisseurFormDefaults = Pick<NewFournisseur, 'id'>;

type FournisseurFormGroupContent = {
  id: FormControl<IFournisseur['id'] | NewFournisseur['id']>;
  prenomNom: FormControl<IFournisseur['prenomNom']>;
  telephone: FormControl<IFournisseur['telephone']>;
  serviceOffert: FormControl<IFournisseur['serviceOffert']>;
  solde: FormControl<IFournisseur['solde']>;
};

export type FournisseurFormGroup = FormGroup<FournisseurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FournisseurFormService {
  createFournisseurFormGroup(fournisseur: FournisseurFormGroupInput = { id: null }): FournisseurFormGroup {
    const fournisseurRawValue = {
      ...this.getFormDefaults(),
      ...fournisseur,
    };
    return new FormGroup<FournisseurFormGroupContent>({
      id: new FormControl(
        { value: fournisseurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      prenomNom: new FormControl(fournisseurRawValue.prenomNom),
      telephone: new FormControl(fournisseurRawValue.telephone),
      serviceOffert: new FormControl(fournisseurRawValue.serviceOffert),
      solde: new FormControl(fournisseurRawValue.solde),
    });
  }

  getFournisseur(form: FournisseurFormGroup): IFournisseur | NewFournisseur {
    return form.getRawValue() as IFournisseur | NewFournisseur;
  }

  resetForm(form: FournisseurFormGroup, fournisseur: FournisseurFormGroupInput): void {
    const fournisseurRawValue = { ...this.getFormDefaults(), ...fournisseur };
    form.reset(
      {
        ...fournisseurRawValue,
        id: { value: fournisseurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FournisseurFormDefaults {
    return {
      id: null,
    };
  }
}
