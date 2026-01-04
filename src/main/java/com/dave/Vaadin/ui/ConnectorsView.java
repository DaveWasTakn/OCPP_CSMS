package com.dave.Vaadin.ui;

import com.dave.Main.State.ChargePoint;
import com.dave.Main.State.Observer;
import com.dave.Main.State.State;
import com.dave.Main.State.StateChangeEvent;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Route("/connectors")
@PageTitle("Connectors")
@Menu(order = 1, icon = "vaadin:connect", title = "Connectors")
public class ConnectorsView extends VerticalLayout implements Observer {

    private UI ui;

    private final State state;
    private final Paragraph status;

    @Autowired
    public ConnectorsView(State state) {
        this.state = state;
        this.state.addObserver(this);

        this.status = new Paragraph();

        add(status);

        updateView();
    }

    private void updateView() {
        this.status.setText(this.state.getChargePoints().stream().map(ChargePoint::toString).collect(Collectors.joining("\n")));
    }


    @Override
    public void onNotify() { // TODO fires too many times ???? investigate why 4 notifications instead of 1
        if (this.ui != null) {
            System.out.println("B");
            this.ui.access(this::updateView);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        this.ui = attachEvent.getUI();
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        this.ui = null;
        super.onDetach(detachEvent);
    }
}
