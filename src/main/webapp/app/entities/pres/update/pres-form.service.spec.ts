import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pres.test-samples';

import { PresFormService } from './pres-form.service';

describe('Pres Form Service', () => {
  let service: PresFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PresFormService);
  });

  describe('Service methods', () => {
    describe('createPresFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPresFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            date: expect.any(Object),
            montant: expect.any(Object),
            fournisseur: expect.any(Object),
          })
        );
      });

      it('passing IPres should create a new form with FormGroup', () => {
        const formGroup = service.createPresFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            date: expect.any(Object),
            montant: expect.any(Object),
            fournisseur: expect.any(Object),
          })
        );
      });
    });

    describe('getPres', () => {
      it('should return NewPres for default Pres initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPresFormGroup(sampleWithNewData);

        const pres = service.getPres(formGroup) as any;

        expect(pres).toMatchObject(sampleWithNewData);
      });

      it('should return NewPres for empty Pres initial value', () => {
        const formGroup = service.createPresFormGroup();

        const pres = service.getPres(formGroup) as any;

        expect(pres).toMatchObject({});
      });

      it('should return IPres', () => {
        const formGroup = service.createPresFormGroup(sampleWithRequiredData);

        const pres = service.getPres(formGroup) as any;

        expect(pres).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPres should not enable id FormControl', () => {
        const formGroup = service.createPresFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPres should disable id FormControl', () => {
        const formGroup = service.createPresFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
