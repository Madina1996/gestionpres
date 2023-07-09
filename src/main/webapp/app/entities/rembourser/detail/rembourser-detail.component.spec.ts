import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RembourserDetailComponent } from './rembourser-detail.component';

describe('Rembourser Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RembourserDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RembourserDetailComponent,
              resolve: { rembourser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(RembourserDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load rembourser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RembourserDetailComponent);

      // THEN
      expect(instance.rembourser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
