import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRembourser, NewRembourser } from '../rembourser.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRembourser for edit and NewRembourserFormGroupInput for create.
 */
type RembourserFormGroupInput = IRembourser | PartialWithRequiredKeyOf<NewRembourser>;

type RembourserFormDefaults = Pick<NewRembourser, 'id'>;

type RembourserFormGroupContent = {
  id: FormControl<IRembourser['id'] | NewRembourser['id']>;
  libelle: FormControl<IRembourser['libelle']>;
  date: FormControl<IRembourser['date']>;
  montant: FormControl<IRembourser['montant']>;
  fournisseur: FormControl<IRembourser['fournisseur']>;
};

export type RembourserFormGroup = FormGroup<RembourserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RembourserFormService {
  createRembourserFormGroup(rembourser: RembourserFormGroupInput = { id: null }): RembourserFormGroup {
    const rembourserRawValue = {
      ...this.getFormDefaults(),
      ...rembourser,
    };
    return new FormGroup<RembourserFormGroupContent>({
      id: new FormControl(
        { value: rembourserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(rembourserRawValue.libelle),
      date: new FormControl(rembourserRawValue.date),
      montant: new FormControl(rembourserRawValue.montant),
      fournisseur: new FormControl(rembourserRawValue.fournisseur),
    });
  }

  getRembourser(form: RembourserFormGroup): IRembourser | NewRembourser {
    return form.getRawValue() as IRembourser | NewRembourser;
  }

  resetForm(form: RembourserFormGroup, rembourser: RembourserFormGroupInput): void {
    const rembourserRawValue = { ...this.getFormDefaults(), ...rembourser };
    form.reset(
      {
        ...rembourserRawValue,
        id: { value: rembourserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RembourserFormDefaults {
    return {
      id: null,
    };
  }
}
