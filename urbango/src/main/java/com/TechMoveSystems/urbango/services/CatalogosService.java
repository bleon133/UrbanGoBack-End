package com.TechMoveSystems.urbango.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class CatalogosService {

    private static final List<String> ID_KEYS = List.of("id", "id_banco", "idbanco", "codigo", "codigo_banco");
    private static final List<String> NAME_KEYS = List.of("nombre", "nombre_banco", "nombrebanco", "banco", "descripcion");
    private static final List<String> CODE_KEYS = List.of("codigo", "codigo_banco", "cod_banco");

    private final JdbcTemplate jdbcTemplate;

    public CatalogosService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> listarBancos() {
        var rows = jdbcTemplate.queryForList("SELECT * FROM bancos ORDER BY 1");
        return rows.stream()
                .map(this::normalizarBanco)
                .filter(Objects::nonNull)
                .toList();
    }

    private Map<String, Object> normalizarBanco(Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            return null;
        }

        var idEntry = findEntry(row, ID_KEYS);
        var nameEntry = findEntry(row, NAME_KEYS);
        var codeEntry = findEntry(row, CODE_KEYS);

        Object idValue = idEntry != null ? idEntry.getValue() : null;
        Object nameValue = nameEntry != null ? nameEntry.getValue() : null;

        if (idValue == null) {
            idValue = firstValue(row);
        }

        if (isBlank(nameValue)) {
            nameValue = firstNonMatchingValue(row, idEntry, codeEntry);
        }

        if (nameValue == null) {
            nameValue = idValue;
        }

        if (idValue == null) {
            idValue = nameValue;
        }

        if (idValue == null || nameValue == null) {
            return null;
        }

        Map<String, Object> normalized = new LinkedHashMap<>();
        normalized.put("id", normalizeId(idValue));
        normalized.put("nombre", String.valueOf(nameValue));
        if (codeEntry != null && !Objects.equals(codeEntry.getValue(), idValue)) {
            normalized.put("codigo", String.valueOf(codeEntry.getValue()));
        }
        return normalized;
    }

    private Map.Entry<String, Object> findEntry(Map<String, Object> row, List<String> candidates) {
        if (row == null) return null;
        for (String candidate : candidates) {
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(candidate)) {
                    return entry;
                }
            }
        }
        return null;
    }

    private Object firstValue(Map<String, Object> row) {
        return row.values().stream().findFirst().orElse(null);
    }

    private Object firstNonMatchingValue(Map<String, Object> row, Map.Entry<String, Object>... exclude) {
        List<String> skipKeys = new ArrayList<>();
        if (exclude != null) {
            for (Map.Entry<String, Object> entry : exclude) {
                if (entry != null && entry.getKey() != null) {
                    skipKeys.add(entry.getKey().toLowerCase(Locale.ROOT));
                }
            }
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            String key = entry.getKey() == null ? null : entry.getKey().toLowerCase(Locale.ROOT);
            if (key != null && skipKeys.contains(key)) continue;
            if (!isBlank(entry.getValue())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private boolean isBlank(Object value) {
        return value == null || value.toString().trim().isEmpty();
    }

    private Object normalizeId(Object value) {
        if (value instanceof Number number) {
            long numeric = number.longValue();
            if (numeric <= Integer.MAX_VALUE && numeric >= Integer.MIN_VALUE) {
                return (int) numeric;
            }
            return numeric;
        }
        if (value == null) {
            return null;
        }
        String text = value.toString().trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            long parsed = Long.parseLong(text);
            if (parsed <= Integer.MAX_VALUE && parsed >= Integer.MIN_VALUE) {
                return (int) parsed;
            }
            return parsed;
        } catch (NumberFormatException ex) {
            return text;
        }
    }
}
