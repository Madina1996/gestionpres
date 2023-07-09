import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRembourser } from '../rembourser.model';
import { RembourserService } from '../service/rembourser.service';

export const rembourserResolve = (route: ActivatedRouteSnapshot): Observable<null | IRembourser> => {
  const id = route.params['id'];
  if (id) {
    return inject(RembourserService)
      .find(id)
      .pipe(
        mergeMap((rembourser: HttpResponse<IRembourser>) => {
          if (rembourser.body) {
            return of(rembourser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default rembourserResolve;
