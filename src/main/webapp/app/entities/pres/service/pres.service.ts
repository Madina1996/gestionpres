import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPres, NewPres } from '../pres.model';

export type PartialUpdatePres = Partial<IPres> & Pick<IPres, 'id'>;

export type EntityResponseType = HttpResponse<IPres>;
export type EntityArrayResponseType = HttpResponse<IPres[]>;

@Injectable({ providedIn: 'root' })
export class PresService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pres: NewPres): Observable<EntityResponseType> {
    return this.http.post<IPres>(this.resourceUrl, pres, { observe: 'response' });
  }

  update(pres: IPres): Observable<EntityResponseType> {
    return this.http.put<IPres>(`${this.resourceUrl}/${this.getPresIdentifier(pres)}`, pres, { observe: 'response' });
  }

  partialUpdate(pres: PartialUpdatePres): Observable<EntityResponseType> {
    return this.http.patch<IPres>(`${this.resourceUrl}/${this.getPresIdentifier(pres)}`, pres, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPres>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPres[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPresIdentifier(pres: Pick<IPres, 'id'>): number {
    return pres.id;
  }

  comparePres(o1: Pick<IPres, 'id'> | null, o2: Pick<IPres, 'id'> | null): boolean {
    return o1 && o2 ? this.getPresIdentifier(o1) === this.getPresIdentifier(o2) : o1 === o2;
  }

  addPresToCollectionIfMissing<Type extends Pick<IPres, 'id'>>(
    presCollection: Type[],
    ...presToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pres: Type[] = presToCheck.filter(isPresent);
    if (pres.length > 0) {
      const presCollectionIdentifiers = presCollection.map(presItem => this.getPresIdentifier(presItem)!);
      const presToAdd = pres.filter(presItem => {
        const presIdentifier = this.getPresIdentifier(presItem);
        if (presCollectionIdentifiers.includes(presIdentifier)) {
          return false;
        }
        presCollectionIdentifiers.push(presIdentifier);
        return true;
      });
      return [...presToAdd, ...presCollection];
    }
    return presCollection;
  }
}
