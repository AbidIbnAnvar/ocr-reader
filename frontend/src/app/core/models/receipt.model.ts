export interface Item {
    name: string;
    price: number;
    quantity?: number;
}
export interface ReceiptResponse {
    id: string,
    imageUrl?: string,
    items: Item[];
    subtotal: number;
    tax: number;
    total: number;
    currencyCode: string;
    date: string
}