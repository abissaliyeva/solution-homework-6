class Logger {
    public static void log(String message) {
        System.out.println(message);
    }
}

abstract class SupportHandler {
    protected SupportHandler handler;
    public SupportHandler set_next(SupportHandler handler){
        this.handler = handler;
        return handler;
    }
    public abstract boolean handle(String issue);
}

class FAQBotHandler extends SupportHandler {
    @Override
    public boolean handle(String issue) {
        if (issue.equals("password_reset")) {
            System.out.println("[FAQBot] Handled password_reset");
            return true;
        }
        return handler != null && handler.handle(issue);
    }
}

class JuniorSupportHandler extends SupportHandler {
    @Override
    public boolean handle(String issue) {
        if (issue.equals("refund_request")) {
            System.out.println("[JuniorSupport] Handled refund_request");
            return true;
        } else if (issue.equals("billing_issue")) {
            System.out.println("[JuniorSupport] Handled billing_issue");
            return true;
        }
        return handler != null && handler.handle(issue);
    }
}

class SeniorSupportHandler extends SupportHandler {
    @Override
    public boolean handle(String issue) {
        if (issue.equals("account_ban")) {
            System.out.println("[SeniorSupport] Handled account_ban");
            return true;
        } else if (issue.equals("data_loss")) {
            System.out.println("[SeniorSupport] Handled data_loss");
            return true;
        }
        System.out.println("[SeniorSupport] Cannot handle " + issue + " — escalate manually");
        throw new RuntimeException("Issue '" + issue + "' could not be resolved");
    }
}

public class Main {
    public static void main(String[] args) {
        SupportHandler faq = new FAQBotHandler();
        faq.set_next(new JuniorSupportHandler()).set_next(new SeniorSupportHandler());

        String[] issues = {"password_reset", "refund_request", "account_ban", "unknown_bug"};

        for (String issue : issues) {
            try {
                boolean resolved = faq.handle(issue);
                if (!resolved) {
                    Logger.log("[Exception] Issue '" + issue + "' went unhandled.");
                }
            } catch (RuntimeException e) {
                Logger.log("[Exception] Issue '" + e.getMessage());
            }
        }
    }
}
