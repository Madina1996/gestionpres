export interface IFournisseur {
  id: number;
  prenomNom?: string | null;
  telephone?: string | null;
  serviceOffert?: string | null;
  solde?: number | null;
}

export type NewFournisseur = Omit<IFournisseur, 'id'> & { id: null };
