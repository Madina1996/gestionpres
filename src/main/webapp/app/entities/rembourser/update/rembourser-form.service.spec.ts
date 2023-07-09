import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rembourser.test-samples';

import { RembourserFormService } from './rembourser-form.service';

describe('Rembourser Form Service', () => {
  let service: RembourserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RembourserFormService);
  });

  describe('Service methods', () => {
    describe('createRembourserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRembourserFormGroup();

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

      it('passing IRembourser should create a new form with FormGroup', () => {
        const formGroup = service.createRembourserFormGroup(sampleWithRequiredData);

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

    describe('getRembourser', () => {
      it('should return NewRembourser for default Rembourser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRembourserFormGroup(sampleWithNewData);

        const rembourser = service.getRembourser(formGroup) as any;

        expect(rembourser).toMatchObject(sampleWithNewData);
      });

      it('should return NewRembourser for empty Rembourser initial value', () => {
        const formGroup = service.createRembourserFormGroup();

        const rembourser = service.getRembourser(formGroup) as any;

        expect(rembourser).toMatchObject({});
      });

      it('should return IRembourser', () => {
        const formGroup = service.createRembourserFormGroup(sampleWithRequiredData);

        const rembourser = service.getRembourser(formGroup) as any;

        expect(rembourser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRembourser should not enable id FormControl', () => {
        const formGroup = service.createRembourserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRembourser should disable id FormControl', () => {
        const formGroup = service.createRembourserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
