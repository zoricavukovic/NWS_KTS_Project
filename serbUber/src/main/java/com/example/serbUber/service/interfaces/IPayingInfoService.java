package com.example.serbUber.service.interfaces;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.token.PayingInfo;

public interface IPayingInfoService {
    PayingInfo getDefaultPayingInfo() throws EntityNotFoundException;
}
