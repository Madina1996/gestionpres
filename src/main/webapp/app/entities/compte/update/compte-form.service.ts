import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompte, NewCompte } from '../compte.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompte for edit and NewCompteFormGroupInput for create.
 */
type CompteFormGroupInput = ICompte | PartialWithRequiredKeyOf<NewCompte>;

type CompteFormDefaults = Pick<NewCompte, 'id'>;

type CompteFormGroupContent = {
  id: FormControl<ICompte['id'] | NewCompte['id']>;
  restant: FormControl<ICompte['restant']>;
  payer: FormControl<ICompte['payer']>;
};

export type CompteFormGroup = FormGroup<CompteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompteFormService {
  createCompteFormGroup(compte: CompteFormGroupInput = { id: null }): CompteFormGroup {
    const compteRawValue = {
      ...this.getFormDefaults(),
      ...compte,
    };
    return new FormGroup<CompteFormGroupContent>({
      id: new FormControl(
        { value: compteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      restant: new FormControl(compteRawValue.restant),
      payer: new FormControl(compteRawValue.payer),
    });
  }

  getCompte(form: CompteFormGroup): ICompte | NewCompte {
    return form.getRawValue() as ICompte | NewCompte;
  }

  resetForm(form: CompteFormGroup, compte: CompteFormGroupInput): void {
    const compteRawValue = { ...this.getFormDefaults(), ...compte };
    form.reset(
      {
        ...compteRawValue,
        id: { value: compteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompteFormDefaults {
    return {
      id: null,
    };
  }
}
