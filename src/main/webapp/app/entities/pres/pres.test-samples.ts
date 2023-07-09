import { IPres, NewPres } from './pres.model';

export const sampleWithRequiredData: IPres = {
  id: 60437,
};

export const sampleWithPartialData: IPres = {
  id: 90654,
  libelle: 'Dodge cultivate navigating',
};

export const sampleWithFullData: IPres = {
  id: 1405,
  libelle: 'Zimbabwe Wooden',
  date: 'Southwest',
  montant: 89383,
};

export const sampleWithNewData: NewPres = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
