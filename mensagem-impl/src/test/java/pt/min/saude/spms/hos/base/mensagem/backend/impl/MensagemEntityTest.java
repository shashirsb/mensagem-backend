package pt.min.saude.spms.hos.base.mensagem.backend.impl;


public class MensagemEntityTest { //TODO
/*
    private static ActorSystem system;
    private static final MensagemId mensagemID = MensagemId.of("HOS-1234", "portugues");

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("MensagemEntityTest");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    private static void withTestDriver(Procedure<PersistentEntityTestDriver<MensagemCommand, MensagemEvent, MensagemState>> block) throws Exception {
        PersistentEntityTestDriver<MensagemCommand, MensagemEvent, MensagemState>
                driver = new PersistentEntityTestDriver<>(system, new MensagemEntity(), mensagemID.toString());
        try {
            block.apply(driver);
        } finally {

        }
    }

    @Test
    public void deveCriarMensagemCentral() throws Exception {
        withTestDriver(driver -> {
            driver.initialize(Optional.empty());

            Mensagem mensagemTeste = new Mensagem(
                    mensagemID,
                    new AlterarMensagemRequest(
                            new DominioValorID(
                                    "tipoMensagem", "info"
                            )
                            , "initial",
                            Option.none(),
                            false
                    )
            );

            PersistentEntityTestDriver.Outcome<MensagemEvent, MensagemState>
                    outcome = driver.run(new MensagemCommand.CreateMensagem(mensagemTeste));

            assertEquals(mensagemTeste, outcome.state().getMensagem());
            assertEquals(1, outcome.events().size());
            assertThat(outcome.events().get(0), is(instanceOf(MensagemEvent.MensagemCreated.class)));
            assertEquals(
                    mensagemTeste,
                    ((MensagemEvent.MensagemCreated) outcome.events().get(0))
                            .getMensagemState().getMensagem()
            );
            assertEquals(Collections.emptyList(), outcome.issues());
        });
    }

    @Test
    public void naoDeveCriarMensagemCentral() throws Exception {
        withTestDriver(driver -> {
            MensagemState initialState = new MensagemState(
                    new Mensagem(
                            mensagemID,
                            new AlterarMensagemRequest(
                                    new DominioValorID(
                                            "tipoMensagem", "error"
                                    ),
                                    "initial",
                                    Option.none(),
                                    false
                            )
                    )
            );

            driver.initialize(Optional.of(initialState));

            Mensagem DOMINIO = new Mensagem(
                    mensagemID,
                    new AlterarMensagemRequest(
                            new DominioValorID(
                                    "tipoMensagem", "error"
                            ),
                            "initial",
                            Option.none(),
                            true
                    )
            );

            PersistentEntityTestDriver.Outcome<MensagemEvent, MensagemState>
                    outcome = driver.run(new MensagemCommand.CreateMensagem(DOMINIO));

            assertThat(outcome.getReplies().get(0), is(instanceOf(PersistentEntity.InvalidCommandException.class)));
            assertEquals(0, outcome.events().size());
            assertEquals(0, outcome.issues().size());
        });
    }


    @Test
    public void deveAlterarMensagemCentral() throws Exception {
        withTestDriver(driver -> {
            MensagemState initialState =
                    new MensagemState(
                            new Mensagem(
                                    mensagemID,
                                    new AlterarMensagemRequest(
                                            new DominioValorID(
                                                    "tipoMensagem", "info"
                                            ),
                                            "initial",
                                            Option.none(),
                                            false
                                    )
                            )
                    );

            driver.initialize(Optional.of(initialState));

            AlterarMensagemRequest.Parciais newAtributos =
                    new AlterarMensagemRequest.Parciais(
                            Option.none(),
                            Option.none(),
                            Option.of("recurso novo"),
                            Option.of(true)
                    );

            PersistentEntityTestDriver.Outcome<MensagemEvent, MensagemState>
                    outcome = driver.run(new MensagemCommand.UpdateMensagem(newAtributos));

            assertNotNull(outcome.getReplies().get(0));
            assertEquals(
                    outcome.state(),
                    initialState.withMensagem(
                            initialState.getMensagem().withAtributos(
                                    initialState.getMensagem().getAtributos().complementar(newAtributos)
                            )
                    )
            );
            assertEquals(1, outcome.events().size());
            assertThat(outcome.events().get(0), is(instanceOf(MensagemEvent.MensagemUpdated.class)));
            assertEquals(
                    newAtributos,
                    ((MensagemEvent.MensagemUpdated) outcome.events().get(0)).getAttributesUpdated()
            );
            assertEquals(Collections.emptyList(), outcome.issues());
        });
    }

    @Test
    public void naoDeveAlteraMensagemCentral() throws Exception {
        withTestDriver(driver -> {
            driver.initialize(Optional.empty());

            AlterarMensagemRequest.Parciais newAtributos =
                    new AlterarMensagemRequest.Parciais(
                            Option.none(),
                            Option.none(),
                            Option.of("recurso novo"),
                            Option.of(true)
                    );

            PersistentEntityTestDriver.Outcome<MensagemEvent, MensagemState>
                    outcome = driver.run(new MensagemCommand.UpdateMensagem(newAtributos));

            assertThat(outcome.getReplies().get(0), is(instanceOf(PersistentEntity.InvalidCommandException.class)));
            assertEquals(0, outcome.events().size());
            assertEquals(0, outcome.issues().size());
        });
    }
*/
}