import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'activite',
        data: { pageTitle: 'Activites' },
        loadChildren: () => import('./activite/activite.routes'),
      },
      {
        path: 'compte',
        data: { pageTitle: 'Comptes' },
        loadChildren: () => import('./compte/compte.routes'),
      },
      {
        path: 'depense',
        data: { pageTitle: 'Depenses' },
        loadChildren: () => import('./depense/depense.routes'),
      },
      {
        path: 'fournisseur',
        data: { pageTitle: 'Fournisseurs' },
        loadChildren: () => import('./fournisseur/fournisseur.routes'),
      },
      {
        path: 'pres',
        data: { pageTitle: 'Pres' },
        loadChildren: () => import('./pres/pres.routes'),
      },
      {
        path: 'rembourser',
        data: { pageTitle: 'Remboursers' },
        loadChildren: () => import('./rembourser/rembourser.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
