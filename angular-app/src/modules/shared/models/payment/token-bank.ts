import { RegularUser } from "../user/regular-user";
import { PayingInfo } from "./paying-info";
import { TokenTransaction } from "./token-transaction";

export interface TokenBank {
    id: number;
    user: RegularUser;
    numOfTokens: number;
    totalTokenAmountSpent: number;
    totalMoneyAmountSpent: number;
    transactions: TokenTransaction[];
    payingInfo: PayingInfo;
}

export interface InAppSpending {
    totalMoneySpent: number;
    totalTokenAmountSpent: number;
    totalTokensInApp: number;
}