package com.example.serbUber.service.payment;

import com.example.serbUber.dto.payment.PayingInfoDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.token.PayingInfo;
import com.example.serbUber.repository.payment.PayingInfoRepository;
import com.example.serbUber.service.interfaces.IPayingInfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.example.serbUber.util.Constants.DEFAULT_PAYING_INFO_ID;

@Component
@Qualifier("payingInfoConfiguration")
public class PayingInfoService implements IPayingInfoService {

    private final PayingInfoRepository payingInfoRepository;

    public PayingInfoService(final PayingInfoRepository payingInfoRepository) {
        this.payingInfoRepository = payingInfoRepository;
    }

    public PayingInfo getDefaultPayingInfo() throws EntityNotFoundException {

        return payingInfoRepository.findById(DEFAULT_PAYING_INFO_ID)
                .orElseThrow(() -> new EntityNotFoundException(DEFAULT_PAYING_INFO_ID, EntityType.PAYING_INFO));
    }

    public PayingInfoDTO update(final double tokenPrice, final int maxNumOfTokensPerTransaction)
            throws EntityNotFoundException
    {
        PayingInfo payingInfo = this.getDefaultPayingInfo();
        payingInfo.setTokenPrice(tokenPrice);
        payingInfo.setMaxNumOfTokensPerTransaction(maxNumOfTokensPerTransaction);

        return new PayingInfoDTO(payingInfoRepository.save(payingInfo));
    }

    public PayingInfoDTO getDefaultPayingInfoDTO() throws EntityNotFoundException {

        return new PayingInfoDTO(this.getDefaultPayingInfo());
    }


}
