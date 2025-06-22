package hotelbao.backend.service;

import hotelbao.backend.repository.EstadiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadiaService {
    @Autowired
    private EstadiaRepository estadiaRepository;

    @Transactional(readOnly = true)
    public Long totalEstadiasCliente (Long id) {
        return estadiaRepository.findSumOfAllClientStays(id);
    }
}
