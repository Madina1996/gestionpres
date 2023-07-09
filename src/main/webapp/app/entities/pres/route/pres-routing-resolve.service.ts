import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPres } from '../pres.model';
import { PresService } from '../service/pres.service';

export const presResolve = (route: ActivatedRouteSnapshot): Observable<null | IPres> => {
  const id = route.params['id'];
  if (id) {
    return inject(PresService)
      .find(id)
      .pipe(
        mergeMap((pres: HttpResponse<IPres>) => {
          if (pres.body) {
            return of(pres.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default presResolve;
