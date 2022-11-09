export class VerifyRequest {
    verifyId: number;
    securityCode: number;
    userRole: string;

    constructor(
        verifyId: number,
        securityCode: number,
        userRole: string
    ) {
        this.verifyId = verifyId;
        this.securityCode = securityCode;
        this.userRole = userRole;
    }

}