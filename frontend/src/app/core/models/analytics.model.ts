export interface AnalyticsData {
  totalSpent: number;
  averagePerReceipt: number;
  receiptsCount: number;
  byDate: {
    date: string;
    amount: number;
  }[];
  byCategory?: {
    category: string;
    amount: number;
  }[];
  byMonth?: {
    month: string;
    amount: number;
  }[];
}
