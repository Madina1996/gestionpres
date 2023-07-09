export interface ICompte {
  id: number;
  restant?: number | null;
  payer?: number | null;
}

export type NewCompte = Omit<ICompte, 'id'> & { id: null };
