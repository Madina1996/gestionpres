import { IFournisseur, NewFournisseur } from './fournisseur.model';

export const sampleWithRequiredData: IFournisseur = {
  id: 17514,
};

export const sampleWithPartialData: IFournisseur = {
  id: 52620,
  prenomNom: 'rhythm incidentally Canadian',
  telephone: '937.889.2374 x104',
};

export const sampleWithFullData: IFournisseur = {
  id: 83273,
  prenomNom: 'calculating',
  telephone: '(518) 252-2092 x949',
  serviceOffert: 'Diesel bypass',
  solde: 11306,
};

export const sampleWithNewData: NewFournisseur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
