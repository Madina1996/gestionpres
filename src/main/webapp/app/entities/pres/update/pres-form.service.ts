import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPres, NewPres } from '../pres.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPres for edit and NewPresFormGroupInput for create.
 */
type PresFormGroupInput = IPres | PartialWithRequiredKeyOf<NewPres>;

type PresFormDefaults = Pick<NewPres, 'id'>;

type PresFormGroupContent = {
  id: FormControl<IPres['id'] | NewPres['id']>;
  libelle: FormControl<IPres['libelle']>;
  date: FormControl<IPres['date']>;
  montant: FormControl<IPres['montant']>;
  fournisseur: FormControl<IPres['fournisseur']>;
};

export type PresFormGroup = FormGroup<PresFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PresFormService {
  createPresFormGroup(pres: PresFormGroupInput = { id: null }): PresFormGroup {
    const presRawValue = {
      ...this.getFormDefaults(),
      ...pres,
    };
    return new FormGroup<PresFormGroupContent>({
      id: new FormControl(
        { value: presRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(presRawValue.libelle),
      date: new FormControl(presRawValue.date),
      montant: new FormControl(presRawValue.montant),
      fournisseur: new FormControl(presRawValue.fournisseur),
    });
  }

  getPres(form: PresFormGroup): IPres | NewPres {
    return form.getRawValue() as IPres | NewPres;
  }

  resetForm(form: PresFormGroup, pres: PresFormGroupInput): void {
    const presRawValue = { ...this.getFormDefaults(), ...pres };
    form.reset(
      {
        ...presRawValue,
        id: { value: presRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PresFormDefaults {
    return {
      id: null,
    };
  }
}
