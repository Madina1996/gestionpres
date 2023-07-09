import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IRembourser {
  id: number;
  libelle?: string | null;
  date?: string | null;
  montant?: number | null;
  fournisseur?: Pick<IFournisseur, 'id'> | null;
}

export type NewRembourser = Omit<IRembourser, 'id'> & { id: null };
