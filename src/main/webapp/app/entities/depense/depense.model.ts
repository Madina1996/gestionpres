import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IDepense {
  id: number;
  libelle?: string | null;
  date?: string | null;
  montant?: number | null;
  fournisseur?: Pick<IFournisseur, 'id'> | null;
}

export type NewDepense = Omit<IDepense, 'id'> & { id: null };
