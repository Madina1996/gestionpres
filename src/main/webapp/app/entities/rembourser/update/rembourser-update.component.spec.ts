import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RembourserFormService } from './rembourser-form.service';
import { RembourserService } from '../service/rembourser.service';
import { IRembourser } from '../rembourser.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

import { RembourserUpdateComponent } from './rembourser-update.component';

describe('Rembourser Management Update Component', () => {
  let comp: RembourserUpdateComponent;
  let fixture: ComponentFixture<RembourserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rembourserFormService: RembourserFormService;
  let rembourserService: RembourserService;
  let fournisseurService: FournisseurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RembourserUpdateComponent],
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
      .overrideTemplate(RembourserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RembourserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rembourserFormService = TestBed.inject(RembourserFormService);
    rembourserService = TestBed.inject(RembourserService);
    fournisseurService = TestBed.inject(FournisseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fournisseur query and add missing value', () => {
      const rembourser: IRembourser = { id: 456 };
      const fournisseur: IFournisseur = { id: 5359 };
      rembourser.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 55628 }];
      jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const additionalFournisseurs = [fournisseur];
      const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
      jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rembourser });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
        fournisseurCollection,
        ...additionalFournisseurs.map(expect.objectContaining)
      );
      expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rembourser: IRembourser = { id: 456 };
      const fournisseur: IFournisseur = { id: 11035 };
      rembourser.fournisseur = fournisseur;

      activatedRoute.data = of({ rembourser });
      comp.ngOnInit();

      expect(comp.fournisseursSharedCollection).toContain(fournisseur);
      expect(comp.rembourser).toEqual(rembourser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRembourser>>();
      const rembourser = { id: 123 };
      jest.spyOn(rembourserFormService, 'getRembourser').mockReturnValue(rembourser);
      jest.spyOn(rembourserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rembourser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rembourser }));
      saveSubject.complete();

      // THEN
      expect(rembourserFormService.getRembourser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rembourserService.update).toHaveBeenCalledWith(expect.objectContaining(rembourser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRembourser>>();
      const rembourser = { id: 123 };
      jest.spyOn(rembourserFormService, 'getRembourser').mockReturnValue({ id: null });
      jest.spyOn(rembourserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rembourser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rembourser }));
      saveSubject.complete();

      // THEN
      expect(rembourserFormService.getRembourser).toHaveBeenCalled();
      expect(rembourserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRembourser>>();
      const rembourser = { id: 123 };
      jest.spyOn(rembourserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rembourser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rembourserService.update).toHaveBeenCalled();
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
