export class VerifyRequest {
    verifyId: string;
    securityCode: number;
    userRole: string;

    constructor(
        verifyId: string,
        securityCode: number,
        userRole: string
    ) {
        this.verifyId = verifyId;
        this.securityCode = securityCode;
        this.userRole = userRole;
    }

}