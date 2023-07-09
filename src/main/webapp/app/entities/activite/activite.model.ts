export interface IActivite {
  id: number;
  libelle?: string | null;
  date?: string | null;
}

export type NewActivite = Omit<IActivite, 'id'> & { id: null };
