import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IPres {
  id: number;
  libelle?: string | null;
  date?: string | null;
  montant?: number | null;
  fournisseur?: Pick<IFournisseur, 'id'> | null;
}

export type NewPres = Omit<IPres, 'id'> & { id: null };
