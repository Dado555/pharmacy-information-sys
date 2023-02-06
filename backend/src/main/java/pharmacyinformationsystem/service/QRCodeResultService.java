package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.QRCodeResult;
import pharmacyinformationsystem.model.users.Patient;

import java.util.List;

public interface QRCodeResultService extends GenericService<QRCodeResult> {

    List<QRCodeResult> getMedicinesFromQR (String data, Integer id);

    Boolean buyMedicinesAfterQR(Integer patientId, Integer qrCodeResultId);

    Boolean sendConfirmationMail(Patient patient, QRCodeResult qrCodeResult);
}
