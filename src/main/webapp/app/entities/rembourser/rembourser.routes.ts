import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RembourserComponent } from './list/rembourser.component';
import { RembourserDetailComponent } from './detail/rembourser-detail.component';
import { RembourserUpdateComponent } from './update/rembourser-update.component';
import RembourserResolve from './route/rembourser-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const rembourserRoute: Routes = [
  {
    path: '',
    component: RembourserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RembourserDetailComponent,
    resolve: {
      rembourser: RembourserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RembourserUpdateComponent,
    resolve: {
      rembourser: RembourserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RembourserUpdateComponent,
    resolve: {
      rembourser: RembourserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rembourserRoute;
