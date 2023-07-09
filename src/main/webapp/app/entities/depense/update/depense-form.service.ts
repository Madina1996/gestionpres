import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDepense, NewDepense } from '../depense.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepense for edit and NewDepenseFormGroupInput for create.
 */
type DepenseFormGroupInput = IDepense | PartialWithRequiredKeyOf<NewDepense>;

type DepenseFormDefaults = Pick<NewDepense, 'id'>;

type DepenseFormGroupContent = {
  id: FormControl<IDepense['id'] | NewDepense['id']>;
  libelle: FormControl<IDepense['libelle']>;
  date: FormControl<IDepense['date']>;
  montant: FormControl<IDepense['montant']>;
  fournisseur: FormControl<IDepense['fournisseur']>;
};

export type DepenseFormGroup = FormGroup<DepenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepenseFormService {
  createDepenseFormGroup(depense: DepenseFormGroupInput = { id: null }): DepenseFormGroup {
    const depenseRawValue = {
      ...this.getFormDefaults(),
      ...depense,
    };
    return new FormGroup<DepenseFormGroupContent>({
      id: new FormControl(
        { value: depenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(depenseRawValue.libelle),
      date: new FormControl(depenseRawValue.date),
      montant: new FormControl(depenseRawValue.montant),
      fournisseur: new FormControl(depenseRawValue.fournisseur),
    });
  }

  getDepense(form: DepenseFormGroup): IDepense | NewDepense {
    return form.getRawValue() as IDepense | NewDepense;
  }

  resetForm(form: DepenseFormGroup, depense: DepenseFormGroupInput): void {
    const depenseRawValue = { ...this.getFormDefaults(), ...depense };
    form.reset(
      {
        ...depenseRawValue,
        id: { value: depenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DepenseFormDefaults {
    return {
      id: null,
    };
  }
}
