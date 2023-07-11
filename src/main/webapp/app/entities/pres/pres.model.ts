import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IPres {
  id: number;
  libelle?: string | null;
  date?: string | null;
  montant?: number | null;
  payer?: number | null;
  restant?: number | null;
  fournisseur?: any | null;
}

export type NewPres = Omit<IPres, 'id'> & { id: null };
