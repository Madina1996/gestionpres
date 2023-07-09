import { ICompte, NewCompte } from './compte.model';

export const sampleWithRequiredData: ICompte = {
  id: 38540,
};

export const sampleWithPartialData: ICompte = {
  id: 46970,
  restant: 89895,
  payer: 77406,
};

export const sampleWithFullData: ICompte = {
  id: 52059,
  restant: 16669,
  payer: 7420,
};

export const sampleWithNewData: NewCompte = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
