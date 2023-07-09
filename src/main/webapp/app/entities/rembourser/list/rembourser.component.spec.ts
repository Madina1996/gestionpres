import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RembourserService } from '../service/rembourser.service';

import { RembourserComponent } from './rembourser.component';

describe('Rembourser Management Component', () => {
  let comp: RembourserComponent;
  let fixture: ComponentFixture<RembourserComponent>;
  let service: RembourserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'rembourser', component: RembourserComponent }]),
        HttpClientTestingModule,
        RembourserComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(RembourserComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RembourserComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RembourserService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.remboursers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to rembourserService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getRembourserIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRembourserIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
