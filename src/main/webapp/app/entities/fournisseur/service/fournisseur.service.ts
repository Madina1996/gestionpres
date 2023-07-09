import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFournisseur, NewFournisseur } from '../fournisseur.model';

export type PartialUpdateFournisseur = Partial<IFournisseur> & Pick<IFournisseur, 'id'>;

export type EntityResponseType = HttpResponse<IFournisseur>;
export type EntityArrayResponseType = HttpResponse<IFournisseur[]>;

@Injectable({ providedIn: 'root' })
export class FournisseurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fournisseurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fournisseur: NewFournisseur): Observable<EntityResponseType> {
    return this.http.post<IFournisseur>(this.resourceUrl, fournisseur, { observe: 'response' });
  }

  update(fournisseur: IFournisseur): Observable<EntityResponseType> {
    return this.http.put<IFournisseur>(`${this.resourceUrl}/${this.getFournisseurIdentifier(fournisseur)}`, fournisseur, {
      observe: 'response',
    });
  }

  partialUpdate(fournisseur: PartialUpdateFournisseur): Observable<EntityResponseType> {
    return this.http.patch<IFournisseur>(`${this.resourceUrl}/${this.getFournisseurIdentifier(fournisseur)}`, fournisseur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFournisseur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFournisseur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFournisseurIdentifier(fournisseur: Pick<IFournisseur, 'id'>): number {
    return fournisseur.id;
  }

  compareFournisseur(o1: Pick<IFournisseur, 'id'> | null, o2: Pick<IFournisseur, 'id'> | null): boolean {
    return o1 && o2 ? this.getFournisseurIdentifier(o1) === this.getFournisseurIdentifier(o2) : o1 === o2;
  }

  addFournisseurToCollectionIfMissing<Type extends Pick<IFournisseur, 'id'>>(
    fournisseurCollection: Type[],
    ...fournisseursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fournisseurs: Type[] = fournisseursToCheck.filter(isPresent);
    if (fournisseurs.length > 0) {
      const fournisseurCollectionIdentifiers = fournisseurCollection.map(
        fournisseurItem => this.getFournisseurIdentifier(fournisseurItem)!
      );
      const fournisseursToAdd = fournisseurs.filter(fournisseurItem => {
        const fournisseurIdentifier = this.getFournisseurIdentifier(fournisseurItem);
        if (fournisseurCollectionIdentifiers.includes(fournisseurIdentifier)) {
          return false;
        }
        fournisseurCollectionIdentifiers.push(fournisseurIdentifier);
        return true;
      });
      return [...fournisseursToAdd, ...fournisseurCollection];
    }
    return fournisseurCollection;
  }
}
