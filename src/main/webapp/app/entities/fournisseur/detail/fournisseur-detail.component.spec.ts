import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FournisseurDetailComponent } from './fournisseur-detail.component';

describe('Fournisseur Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FournisseurDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FournisseurDetailComponent,
              resolve: { fournisseur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(FournisseurDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load fournisseur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FournisseurDetailComponent);

      // THEN
      expect(instance.fournisseur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
