import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PresFormService } from './pres-form.service';
import { PresService } from '../service/pres.service';
import { IPres } from '../pres.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

import { PresUpdateComponent } from './pres-update.component';

describe('Pres Management Update Component', () => {
  let comp: PresUpdateComponent;
  let fixture: ComponentFixture<PresUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let presFormService: PresFormService;
  let presService: PresService;
  let fournisseurService: FournisseurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PresUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PresUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PresUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    presFormService = TestBed.inject(PresFormService);
    presService = TestBed.inject(PresService);
    fournisseurService = TestBed.inject(FournisseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fournisseur query and add missing value', () => {
      const pres: IPres = { id: 456 };
      const fournisseur: IFournisseur = { id: 7415 };
      pres.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 25718 }];
      jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const additionalFournisseurs = [fournisseur];
      const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
      jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pres });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
        fournisseurCollection,
        ...additionalFournisseurs.map(expect.objectContaining)
      );
      expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pres: IPres = { id: 456 };
      const fournisseur: IFournisseur = { id: 81695 };
      pres.fournisseur = fournisseur;

      activatedRoute.data = of({ pres });
      comp.ngOnInit();

      expect(comp.fournisseursSharedCollection).toContain(fournisseur);
      expect(comp.pres).toEqual(pres);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPres>>();
      const pres = { id: 123 };
      jest.spyOn(presFormService, 'getPres').mockReturnValue(pres);
      jest.spyOn(presService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pres }));
      saveSubject.complete();

      // THEN
      expect(presFormService.getPres).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(presService.update).toHaveBeenCalledWith(expect.objectContaining(pres));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPres>>();
      const pres = { id: 123 };
      jest.spyOn(presFormService, 'getPres').mockReturnValue({ id: null });
      jest.spyOn(presService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pres: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pres }));
      saveSubject.complete();

      // THEN
      expect(presFormService.getPres).toHaveBeenCalled();
      expect(presService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPres>>();
      const pres = { id: 123 };
      jest.spyOn(presService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pres });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(presService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFournisseur', () => {
      it('Should forward to fournisseurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fournisseurService, 'compareFournisseur');
        comp.compareFournisseur(entity, entity2);
        expect(fournisseurService.compareFournisseur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
