import { IDepense, NewDepense } from './depense.model';

export const sampleWithRequiredData: IDepense = {
  id: 61295,
};

export const sampleWithPartialData: IDepense = {
  id: 92054,
  libelle: 'leverage Directives',
  date: 'alarm East',
  montant: 36325,
};

export const sampleWithFullData: IDepense = {
  id: 15319,
  libelle: 'sprout',
  date: 'Agent dearest',
  montant: 10580,
};

export const sampleWithNewData: NewDepense = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
