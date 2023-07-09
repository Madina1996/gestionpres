import { IRembourser, NewRembourser } from './rembourser.model';

export const sampleWithRequiredData: IRembourser = {
  id: 12069,
};

export const sampleWithPartialData: IRembourser = {
  id: 42902,
  date: 'withdrawal East',
  montant: 1431,
};

export const sampleWithFullData: IRembourser = {
  id: 32369,
  libelle: 'kilogram',
  date: 'male SUV project',
  montant: 20078,
};

export const sampleWithNewData: NewRembourser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
