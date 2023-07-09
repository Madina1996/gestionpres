import { IActivite, NewActivite } from './activite.model';

export const sampleWithRequiredData: IActivite = {
  id: 8300,
};

export const sampleWithPartialData: IActivite = {
  id: 37080,
  date: 'quantifying transmitting doloribus',
};

export const sampleWithFullData: IActivite = {
  id: 43144,
  libelle: 'yellow Jewelery',
  date: 'yahoo monitoring',
};

export const sampleWithNewData: NewActivite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
