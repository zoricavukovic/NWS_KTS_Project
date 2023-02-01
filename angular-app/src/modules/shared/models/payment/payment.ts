export interface CreatePayment {
    tokenBankId: number;
    numOfTokens: number;
    payerId?: string;
    paymentId?: string;
}

export interface RedirectInfo {
    status?: string,
    redirectUrl?: string;
}
