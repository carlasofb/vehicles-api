package com.example.vehicles.vehicle;

import java.util.List;

/** Uma "fatia" da lista: os itens da página e se há mais (sem fazer COUNT(*) ao total). */
public record VehicleSlice(List<VehicleResponse> content, int page, int size, boolean hasNext) {
}