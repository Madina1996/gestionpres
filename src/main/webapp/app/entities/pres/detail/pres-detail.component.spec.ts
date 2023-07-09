import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PresDetailComponent } from './pres-detail.component';

describe('Pres Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PresDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PresDetailComponent,
              resolve: { pres: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(PresDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load pres on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PresDetailComponent);

      // THEN
      expect(instance.pres).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
