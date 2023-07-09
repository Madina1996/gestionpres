import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PresComponent } from './list/pres.component';
import { PresDetailComponent } from './detail/pres-detail.component';
import { PresUpdateComponent } from './update/pres-update.component';
import PresResolve from './route/pres-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const presRoute: Routes = [
  {
    path: '',
    component: PresComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PresDetailComponent,
    resolve: {
      pres: PresResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PresUpdateComponent,
    resolve: {
      pres: PresResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PresUpdateComponent,
    resolve: {
      pres: PresResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default presRoute;
