jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { RembourserService } from '../service/rembourser.service';

import { RembourserDeleteDialogComponent } from './rembourser-delete-dialog.component';

describe('Rembourser Management Delete Component', () => {
  let comp: RembourserDeleteDialogComponent;
  let fixture: ComponentFixture<RembourserDeleteDialogComponent>;
  let service: RembourserService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RembourserDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(RembourserDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RembourserDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RembourserService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
