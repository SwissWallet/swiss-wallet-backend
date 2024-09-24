package com.swiss.wallet.entity;

public enum StatusBenefit {
    SENT,              // Solicitação enviada
    UNDER_ANALYSIS,   // Em análise
    APPROVED,         // Aprovado
    NOT_APPROVED,     // Negado
    PENDING_DOCUMENTS, // Pendência de documentos
    IN_PROGRESS,      // Em andamento
    CLOSED             // Finalizada
}
