
// Add some interactivity
document.addEventListener('DOMContentLoaded', function() {
    // Animate score card on load
    const scoreCard = document.querySelector('.score-card');
    if (scoreCard) {
        scoreCard.style.transform = 'scale(0.9)';
        scoreCard.style.opacity = '0';

        setTimeout(() => {
            scoreCard.style.transition = 'all 0.5s ease';
            scoreCard.style.transform = 'scale(1)';
            scoreCard.style.opacity = '1';
        }, 300);
    }

    // Add click to copy result ID functionality
    const resultInfo = document.querySelector('.result-info');
    if (resultInfo) {
        resultInfo.style.cursor = 'pointer';
        resultInfo.title = 'Click to copy Result ID';

        resultInfo.addEventListener('click', function() {
            const resultId = '${testResult.resultId}';
            if (navigator.clipboard && resultId) {
                navigator.clipboard.writeText(resultId).then(() => {
                    // Show temporary success message
                    const originalBg = this.style.backgroundColor;
                    const originalBorder = this.style.borderLeftColor;

                    this.style.backgroundColor = '#d4edda';
                    this.style.borderLeftColor = '#28a745';

                    setTimeout(() => {
                        this.style.backgroundColor = originalBg || '#e3f2fd';
                        this.style.borderLeftColor = originalBorder || '#2196f3';
                    }, 2000);
                }).catch(err => {
                    console.log('Failed to copy: ', err);
                });
            }
        });
    }

    // Add smooth scroll for question review
    const questions = document.querySelectorAll('.question-item');
    questions.forEach((question, index) => {
        question.style.opacity = '0';
        question.style.transform = 'translateY(20px)';

        setTimeout(() => {
            question.style.transition = 'all 0.3s ease';
            question.style.opacity = '1';
            question.style.transform = 'translateY(0)';
        }, (index + 1) * 100);
    });
});
