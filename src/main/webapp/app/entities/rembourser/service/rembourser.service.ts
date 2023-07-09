import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRembourser, NewRembourser } from '../rembourser.model';

export type PartialUpdateRembourser = Partial<IRembourser> & Pick<IRembourser, 'id'>;

export type EntityResponseType = HttpResponse<IRembourser>;
export type EntityArrayResponseType = HttpResponse<IRembourser[]>;

@Injectable({ providedIn: 'root' })
export class RembourserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/remboursers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rembourser: NewRembourser): Observable<EntityResponseType> {
    return this.http.post<IRembourser>(this.resourceUrl, rembourser, { observe: 'response' });
  }

  update(rembourser: IRembourser): Observable<EntityResponseType> {
    return this.http.put<IRembourser>(`${this.resourceUrl}/${this.getRembourserIdentifier(rembourser)}`, rembourser, {
      observe: 'response',
    });
  }

  partialUpdate(rembourser: PartialUpdateRembourser): Observable<EntityResponseType> {
    return this.http.patch<IRembourser>(`${this.resourceUrl}/${this.getRembourserIdentifier(rembourser)}`, rembourser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRembourser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRembourser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRembourserIdentifier(rembourser: Pick<IRembourser, 'id'>): number {
    return rembourser.id;
  }

  compareRembourser(o1: Pick<IRembourser, 'id'> | null, o2: Pick<IRembourser, 'id'> | null): boolean {
    return o1 && o2 ? this.getRembourserIdentifier(o1) === this.getRembourserIdentifier(o2) : o1 === o2;
  }

  addRembourserToCollectionIfMissing<Type extends Pick<IRembourser, 'id'>>(
    rembourserCollection: Type[],
    ...remboursersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const remboursers: Type[] = remboursersToCheck.filter(isPresent);
    if (remboursers.length > 0) {
      const rembourserCollectionIdentifiers = rembourserCollection.map(rembourserItem => this.getRembourserIdentifier(rembourserItem)!);
      const remboursersToAdd = remboursers.filter(rembourserItem => {
        const rembourserIdentifier = this.getRembourserIdentifier(rembourserItem);
        if (rembourserCollectionIdentifiers.includes(rembourserIdentifier)) {
          return false;
        }
        rembourserCollectionIdentifiers.push(rembourserIdentifier);
        return true;
      });
      return [...remboursersToAdd, ...rembourserCollection];
    }
    return rembourserCollection;
  }
}
